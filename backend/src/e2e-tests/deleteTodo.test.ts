import { TodosRepository } from "../repositories/TodosRepository";
import request from "supertest";
import { app } from "../router";
import expect from "expect";
import { ApiError } from "../model/ApiError";

describe('DELETE /todos/:id', () => {
  const deleteSpy = jest.spyOn(TodosRepository, 'delete');

  beforeEach(() => jest.clearAllMocks())

  it('should delete a todo', async () => {
    deleteSpy.mockResolvedValue();

    await request(app)
      .delete('/todos/123')
      .expect(204);
  });

  it('should return 500 on database error', async () => {
    deleteSpy.mockRejectedValue(new Error('db error'));

    const response = await request(app)
      .delete('/todos/123')
      .expect(500);

    expect(response.body).toEqual({ message: "Internal server error" } satisfies ApiError);
  });
});
