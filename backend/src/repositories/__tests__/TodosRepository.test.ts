import { TodosRepository } from '../TodosRepository';
import {
  PutCommand,
  ScanCommand,
  DeleteCommand,
  UpdateCommand,
} from '@aws-sdk/lib-dynamodb';
import { Todo } from "../../model/Todo";
import { UpdateTodoRequest } from "../../model/UpdateTodoRequest";

describe('TodosRepository', () => {
  let mockSend: jest.Mock;

  beforeAll(() => {
    mockSend = jest.fn();
    // @ts-ignore
    TodosRepository['ddbDocClient'] = { send: mockSend };
  });

  afterEach(() => jest.resetAllMocks());

  describe('create', () => {
    it('should send PutCommand with correct parameters', async () => {
      mockSend.mockResolvedValue({});
      const todo: Todo = { id: '1', content: 'sth', completed: false, createdAt: new Date().toISOString() };

      await TodosRepository.create(todo);

      expect(mockSend).toHaveBeenCalledWith(expect.any(PutCommand));
      const sentCommand = mockSend.mock.calls[0][0];
      expect(sentCommand.input.TableName).toBeDefined();
      expect(sentCommand.input.Item).toEqual(todo);
    });
  });

  describe('listAll', () => {
    it('should send ScanCommand and return items', async () => {
      const todos: Todo[] = [
        { id: '1', content: 'task1', completed: false, createdAt: new Date().toISOString() },
        { id: '2', content: 'task2', completed: true, createdAt: new Date().toISOString() },
      ];
      mockSend.mockResolvedValue({ Items: todos });

      const result = await TodosRepository.listAll();

      expect(mockSend).toHaveBeenCalledWith(expect.any(ScanCommand));
      expect(result).toEqual(todos);
    });

    it('should return empty array if no items', async () => {
      mockSend.mockResolvedValue({});

      const result = await TodosRepository.listAll();

      expect(result).toEqual([]);
    });
  });

  describe('delete', () => {
    it('should send DeleteCommand with correct key', async () => {
      mockSend.mockResolvedValue({});
      const id = '1';

      await TodosRepository.delete(id);

      expect(mockSend).toHaveBeenCalledWith(expect.any(DeleteCommand));
      const sentCommand = mockSend.mock.calls[0][0];
      expect(sentCommand.input.TableName).toBeDefined();
      expect(sentCommand.input.Key).toEqual({ id });
    });
  });

  describe('update', () => {
    const id = '1';
    const newValues: UpdateTodoRequest = { content: 'updated', completed: true };

    it('should return updated item on success', async () => {
      const updatedTodo: Todo = { id, ...newValues, createdAt: new Date().toISOString() };
      mockSend.mockResolvedValue({ Attributes: updatedTodo });

      const result = await TodosRepository.update(id, newValues);

      expect(mockSend).toHaveBeenCalledWith(expect.any(UpdateCommand));
      expect(result).toEqual(updatedTodo);
    });

    it('should return null if ConditionalCheckFailedException', async () => {
      const error = new Error('Conditional check failed');
      error.name = 'ConditionalCheckFailedException';
      mockSend.mockRejectedValue(error);

      const result = await TodosRepository.update(id, newValues);

      expect(result).toBeNull();
    });

    it('should throw error if other error occurs', async () => {
      const error = new Error('a different error');
      mockSend.mockRejectedValue(error);

      await expect(TodosRepository.update(id, newValues)).rejects.toThrow(error);
    });
  });
});
