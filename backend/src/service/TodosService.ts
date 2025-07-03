import { Todo } from "../model/Todo";
import { v4 as uuidv4 } from "uuid";
import { TodosRepository } from "../repositories/TodosRepository";
import { CreateTodoRequest } from "../model/CreateTodoRequest";
import { UpdateTodoRequest } from "../model/UpdateTodoRequest";

export class TodosService {
  public static async saveNewTodo(createTodoRequest: CreateTodoRequest): Promise<Todo> {
    const newTodo: Todo = {
      id: uuidv4(),
      content: createTodoRequest.content,
      createdAt: new Date().toISOString(),
      completed: false,
    };
    await TodosRepository.create(newTodo);
    return newTodo;
  }

  public static async listAll(): Promise<Todo[]> {
    return TodosRepository.listAll();
  }

  public static async deleteOne(id: string): Promise<void> {
    return TodosRepository.delete(id);
  }

  static async update(id: string, newValues: UpdateTodoRequest): Promise<Todo | null> {
    return TodosRepository.update(id, newValues);
  }
}
