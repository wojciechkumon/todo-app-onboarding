import { TodosRepository } from "../repositories/TodosRepository";
import { v4 as uuidv4 } from "uuid";
import { UpdateTodoRequest } from "../model/UpdateTodoRequest";
import { Todo } from "../model/Todo";
import request from "supertest";
import { app } from "../router";
import expect from "expect";
import { ApiError } from "../model/ApiError";

describe('PUT /todos/:id', () => {
  const updateSpy = jest.spyOn(TodosRepository, 'update');
  const id = uuidv4();
  const requestBody: UpdateTodoRequest = { content: 'new content', completed: true };

  beforeEach(() => jest.clearAllMocks())

  it('should update an existing todo', async () => {
    const updatedTodo: Todo = {
      id,
      content: requestBody.content,
      completed: requestBody.completed,
      createdAt: new Date().toISOString()
    };
    updateSpy.mockResolvedValue(updatedTodo);

    const response = await request(app)
      .put(`/todos/${id}`)
      .send(requestBody)
      .expect(200);

    expect(response.body).toEqual(updatedTodo);
    expect(updateSpy).toHaveBeenCalledWith(id, requestBody)
  });

  it('should return 404 on not existing todo', async () => {
    const unknownId = 'unknown-id';
    const requestBody: UpdateTodoRequest = { content: 'new content', completed: true };
    updateSpy.mockResolvedValue(null);

    await request(app)
      .put(`/todos/${unknownId}`)
      .send(requestBody)
      .expect(404);

    expect(updateSpy).toHaveBeenCalledWith(unknownId, requestBody)
  });

  it('should return 500 on database error', async () => {
    updateSpy.mockRejectedValue(new Error('db error'));

    const response = await request(app)
      .put(`/todos/${id}`)
      .send(requestBody)
      .expect(500);

    expect(response.body).toEqual({ message: "Internal server error" } satisfies ApiError);
  });

  describe('Invalid request payload', () => {
    it('should return 400 when completed is missing', async () => {
      const response = await request(app)
        .put(`/todos/${id}`)
        .send({ content: 'new content' })
        .expect(400);

      expect(response.body).toMatchObject({
        message: 'Invalid input',
        errors: [{
          instancePath: "",
          keyword: "required",
          message: "must have required property 'completed'",
          params: { missingProperty: "completed" },
          schemaPath: "#/required",
        }],
      } satisfies ApiError);
    });

    it('should return 400 when content is empty string', async () => {
      const response = await request(app)
        .put(`/todos/${id}`)
        .send({ content: '', completed: true })
        .expect(400);

      expect(response.body).toMatchObject({
        message: 'Invalid input',
        errors: [{
          instancePath: "/content",
          keyword: "minLength",
          message: "must NOT have fewer than 1 characters",
          params: { "limit": 1 },
          schemaPath: "#/properties/content/minLength",
        }],
      } satisfies ApiError);
    });
  });
});
