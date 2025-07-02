import express, { Request, Response } from 'express';
import cors from 'cors';
import { CreateTodoRequest, validateCreateTodo } from "./model/CreateTodoRequest";
import { Todo } from "./model/Todo";
import { ApiError } from "./model/ApiError";

export const app = express();
app.use(cors());
app.use(express.json());

app.post("/todos", (req: Request, res: Response<Todo | ApiError>) => {
  if (!validateCreateTodo(req.body)) {
    return res.status(400).json({
      message: "Invalid input",
      errors: validateCreateTodo.errors ?? undefined,
    } satisfies ApiError);
  }
  const requestBody: CreateTodoRequest = req.body;
  const newTodo: Todo = {
    id: 1,
    content: requestBody.content,
    completed: false,
  };

  return res.status(201).json(newTodo);
});

app.get("/status", (req: Request, res: Response) =>
  res.status(200).json({ status: "OK" }));

app.use((req: Request, res: Response) =>
  res.status(404).json({ error: "Not Found" }));
