import request from 'supertest';
import { app } from '../router';
import { Todo } from '../model/Todo';
import { ApiError } from '../model/ApiError';
import { CreateTodoRequest } from "../model/CreateTodoRequest";

describe('POST /todos', () => {
  it('should create a new todo with valid request', async () => {
    const validRequest: CreateTodoRequest = { content: 'Test todo item' };

    const response = await request(app)
      .post('/todos')
      .send(validRequest)
      .expect(201);

    expect(response.body).toEqual({
      id: 1,
      content: 'Test todo item',
      completed: false
    } satisfies Todo);
  });

  describe('Invalid request payload', () => {
    it('should return 400 when content is missing', async () => {
      const invalidRequest = {};

      const response = await request(app)
        .post('/todos')
        .send(invalidRequest)
        .expect(400);

      expect(response.body).toMatchObject({
        message: 'Invalid input',
        errors: [{
          instancePath: "",
          keyword: "required",
          message: "must have required property 'content'",
          params: { missingProperty: "content" },
          schemaPath: "#/required",
        }],
      } satisfies ApiError);
    });

    it('should return 400 when content is empty string', async () => {
      const invalidRequest: CreateTodoRequest = { content: '' };

      const response = await request(app)
        .post('/todos')
        .send(invalidRequest)
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
