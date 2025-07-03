import request from 'supertest';
import { app } from '../router';
import { ApiError } from '../model/ApiError';
import { CreateTodoRequest } from "../model/CreateTodoRequest";
import { TodosRepository } from "../repositories/TodosRepository";
import expect from "expect";

describe('POST /todos', () => {
  const createSpy = jest.spyOn(TodosRepository, 'create');

  beforeEach(() => jest.clearAllMocks())

  it('should create a new todo with valid request', async () => {
    createSpy.mockResolvedValue();
    const validRequest: CreateTodoRequest = { content: 'Test todo item' };

    const response = await request(app)
      .post('/todos')
      .send(validRequest)
      .expect(201);

    const expected = {
      id: expect.any(String),
      content: 'Test todo item',
      completed: false,
      createdAt: expect.any(String),
    };
    expect(response.body).toEqual(expected);
    expect(createSpy).toHaveBeenCalledWith(expected)
  });

  it('should return 500 on database error', async () => {
    createSpy.mockRejectedValue(new Error('db error'));
    const validRequest: CreateTodoRequest = { content: 'Test todo item' };

    const response = await request(app)
      .post('/todos')
      .send(validRequest)
      .expect(500);

    expect(response.body).toEqual({ message: "Internal server error" } satisfies ApiError);
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
