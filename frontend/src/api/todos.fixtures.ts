import type { Todo } from 'src/api/todos';

export const createTodo = ({
  id,
  content,
  completed,
  createdAt,
}: Partial<Todo> & { id: string; content: string }): Todo => ({
  id,
  content,
  completed: completed ?? false,
  createdAt: createdAt ?? new Date().toISOString(),
});
