import { Request, Response, Router } from "express";
import { Todo } from "../model/Todo";
import { ApiError } from "../model/ApiError";
import { TodosService } from "../service/TodosService";
import { CreateTodoRequest, validateCreateTodo } from "../model/CreateTodoRequest";
import { UpdateTodoRequest, validateUpdateTodo } from "../model/UpdateTodoRequest";

export const todosController = Router();

todosController.get("/", async (req: Request, res: Response<Todo[] | ApiError>) => {
  try {
    const allTodos = await TodosService.listAll();
    return res.status(200).json(allTodos);
  } catch (e) {
    console.error('Sth went wrong on GET /todos', { message: (e as Error).message, stack: (e as Error).stack });
    return res.status(500).json({ message: 'Internal server error' });
  }
});

todosController.post("/", async (req: Request, res: Response<Todo | ApiError>) => {
  if (!validateCreateTodo(req.body)) {
    return res.status(400).json({
      message: "Invalid input",
      errors: validateCreateTodo.errors ?? undefined,
    } satisfies ApiError);
  }
  const requestBody: CreateTodoRequest = req.body;
  try {
    const newTodo = await TodosService.saveNewTodo(requestBody);
    return res.status(201).json(newTodo);
  } catch (e) {
    console.error('Sth went wrong on POST /todos', { message: (e as Error).message, stack: (e as Error).stack });
    return res.status(500).json({ message: 'Internal server error' });
  }
})

todosController.delete("/:id", async (req: Request, res: Response<Todo[] | ApiError>) => {
  const { id } = req.params;
  try {
    await TodosService.deleteOne(id);
    return res.sendStatus(204);
  } catch (e) {
    console.error('Sth went wrong on DELETE /todos/:id', {
      message: (e as Error).message,
      stack: (e as Error).stack
    });
    return res.status(500).json({ message: 'Internal server error' });
  }
});

todosController.put("/:id", async (req: Request, res: Response<Todo | ApiError>) => {
  if (!validateUpdateTodo(req.body)) {
    return res.status(400).json({
      message: "Invalid input",
      errors: validateUpdateTodo.errors ?? undefined,
    } satisfies ApiError);
  }
  const requestBody: UpdateTodoRequest = req.body;
  const { id } = req.params;

  try {
    const updatedTodo = await TodosService.update(id, requestBody);
    if (!updatedTodo) {
      return res.sendStatus(404);
    }
    return res.status(200).json(updatedTodo);
  } catch (e) {
    console.error('Sth went wrong on PUT /todos/:id', { message: (e as Error).message, stack: (e as Error).stack });
    return res.status(500).json({ message: 'Internal server error' });
  }
});
