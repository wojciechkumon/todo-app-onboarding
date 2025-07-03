import express, { Request, Response } from 'express';
import cors from 'cors';
import { todosController } from "./controller/todosController";

export const app = express();
app.use(cors());
app.use(express.json());

app.use("/todos", todosController);

app.get("/status", (req: Request, res: Response) =>
  res.status(200).json({ status: "OK" }));

app.use((req: Request, res: Response) =>
  res.status(404).json({ error: "Not Found" }));
