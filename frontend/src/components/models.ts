import type { Todo } from 'src/api/todos';

export interface TodoViewModel extends Todo {
  isDuringUpdate?: boolean;
}
