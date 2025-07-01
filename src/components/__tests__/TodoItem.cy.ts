import TodoItem from '../TodoItem.vue';
import type { Todo } from 'components/models';

describe('TodoItem', () => {
  const todo: Todo = { id: 1, content: 'Test todo item' };

  it('renders todo content and buttons', () => {
    cy.mount(TodoItem, { props: { todo } });

    cy.contains('Test todo item').should('be.visible');
    cy.get('[data-cy=edit-btn]').should('exist');
    cy.get('[data-cy=delete-btn]').should('exist');
  });
});
