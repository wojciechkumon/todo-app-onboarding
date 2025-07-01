import TodoList from '../TodoList.vue';
import type { Todo } from 'components/models';

describe('TodoList', () => {
  const todos: Todo[] = [
    { id: 1, content: 'First todo' },
    { id: 2, content: 'Second todo' },
    { id: 3, content: 'Third todo' },
  ];

  it('renders todos and header', () => {
    cy.mount(TodoList, {
      props: { todos },
    });

    cy.contains('Your todos').should('be.visible');
    todos.forEach((todo) => {
      cy.contains(todo.content).should('be.visible');
    });
    cy.get('[data-cy=todo-item]').should('have.length', todos.length);
  });
});
