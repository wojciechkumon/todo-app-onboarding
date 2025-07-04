import axios from 'axios';
import { config } from 'src/config';

interface CreateTodoRequest {
  content: string;
}

interface UpdateTodoRequest {
  content: string;
  completed: boolean;
}

export interface Todo {
  id: string;
  content: string;
  completed: boolean;
  createdAt: string;
}

const create = async (content: string): Promise<Todo> => {
  const response = await axios.post(
    config.apiBaseUrl + '/todos',
    { content } satisfies CreateTodoRequest,
    { validateStatus: (status) => status === 201 },
  );
  return response.data;
};

const fetchAll = async (): Promise<Todo[]> => {
  const response = await axios.get(config.apiBaseUrl + '/todos', {
    validateStatus: (status) => status === 200,
  });
  return response.data;
};

const deleteOne = async (id: string): Promise<void> => {
  await axios.delete(`${config.apiBaseUrl}/todos/${id}`, {
    validateStatus: (status) => status === 204,
  });
};

const updateOne = async (id: string, request: UpdateTodoRequest): Promise<Todo> => {
  const response = await axios.put(`${config.apiBaseUrl}/todos/${id}`, request, {
    validateStatus: (status) => status === 200,
  });
  return response.data;
};

export const TodosApi = {
  create,
  fetchAll,
  deleteOne,
  updateOne,
};
