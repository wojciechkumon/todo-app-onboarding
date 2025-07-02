import axios from 'axios';
import { config } from 'src/config';
import type { Todo } from 'components/models';

interface CreateTodoRequest {
  content: string;
}

export const createTodo = async (content: string): Promise<Todo> => {
  const response = await axios.post(
    config.apiBaseUrl + '/todos',
    { content } satisfies CreateTodoRequest,
    { validateStatus: (status) => status === 201 },
  );
  return response.data;
};
