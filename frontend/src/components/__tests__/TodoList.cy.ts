import TodoList from '../TodoList.vue';
import type { Todo } from 'src/api/todos';
import { createTodo } from 'src/api/todos.fixtures';

describe('TodoList', () => {
  const todos: Todo[] = [
    createTodo({ id: '1', content: 'First todo' }),
    createTodo({ id: '2', content: 'Second todo', completed: true }),
    createTodo({ id: '3', content: 'Third todo' }),
  ];

  it('renders active and completed todos', () => {
    cy.mount(TodoList, {
      props: { todos },
    });

    cy.contains('Your todos').should('be.visible');
    cy.contains('Completed todos').should('be.visible');
    todos.forEach((todo) => {
      cy.contains(todo.content).should('be.visible');
    });

    cy.get('[data-cy=todo-item]').should('have.length', 2);
    cy.get('[data-cy=completed-todo-item]').should('have.length', 1);
    cy.contains('Add your first todo above').should('not.exist');
  });

  it('renders empty todos list', () => {
    cy.mount(TodoList, {
      props: { todos: [] },
    });

    cy.contains('Add your first todo above').should('be.visible');
    cy.contains('Your todos').should('not.exist');
  });

  it('emits toggle-complete event when checkbox is clicked', () => {
    const onToggleComplete = cy.spy().as('onToggleComplete');

    cy.mount(TodoList, {
      props: { todos },
      attrs: { 'onToggle-complete': (id: string) => onToggleComplete(id) },
    });

    cy.get('[data-cy=todo-checkbox]').first().click();
    cy.get('@onToggleComplete').should('have.been.called');
  });
});
