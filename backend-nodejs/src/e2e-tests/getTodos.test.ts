import { TodosRepository } from "../repositories/TodosRepository";
import { Todo } from "../model/Todo";
import { v4 as uuidv4 } from "uuid";
import request from "supertest";
import { app } from "../router";
import expect from "expect";
import { ApiError } from "../model/ApiError";

describe('GET /todos', () => {
  const listAllSpy = jest.spyOn(TodosRepository, 'listAll');

  beforeEach(() => jest.clearAllMocks())

  it('should list all todos', async () => {
    const todos: Todo[] = [
      { id: uuidv4(), content: 'todo1', completed: false, createdAt: new Date().toISOString() },
      { id: uuidv4(), content: 'todo2', completed: false, createdAt: new Date().toISOString() },
    ]
    listAllSpy.mockResolvedValue(todos);

    const response = await request(app)
      .get('/todos')
      .expect(200);

    expect(response.body).toEqual(todos);
  });

  it('should return 500 on database error', async () => {
    listAllSpy.mockRejectedValue(new Error('db error'));

    const response = await request(app)
      .get('/todos')
      .expect(500);

    expect(response.body).toEqual({ message: "Internal server error" } satisfies ApiError);
  });
});
