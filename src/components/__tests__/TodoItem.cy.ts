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

  it('emits delete event when delete button is clicked', () => {
    const onDelete = cy.spy().as('onDeleteSpy');

    cy.mount(TodoItem, {
      props: { todo },
      attrs: { onDelete },
    });

    cy.get('[data-cy=delete-btn]').click();
    cy.get('@onDeleteSpy').should('have.been.calledOnce');
  });

  describe('edit mode', () => {
    it('enters edit mode when edit button is clicked', () => {
      cy.mount(TodoItem, { props: { todo } });

      cy.get('[data-cy=edit-btn]').click();

      cy.get('[data-cy=edit-input]').should('exist').should('have.value', todo.content);
      cy.get('[data-cy=edit-save-btn]').should('exist');
      cy.get('[data-cy=edit-cancel-btn]').should('exist');
      cy.get('[data-cy=edit-btn]').should('not.exist');
      cy.get('[data-cy=delete-btn]').should('not.exist');
    });

    it('emits edit event when text is changed and saved', () => {
      const onEdit = cy.spy().as('onEditSpy');
      const newContent = 'Updated content';

      cy.mount(TodoItem, {
        props: { todo },
        attrs: { onEdit },
      });

      cy.get('[data-cy=edit-btn]').click();
      cy.get('[data-cy=edit-input]').clear().type(newContent);
      cy.get('[data-cy=edit-save-btn]').click();

      cy.get('@onEditSpy').should('have.been.calledOnceWith', newContent);
    });

    it('cancels edit and restores original content on cancel button click', () => {
      cy.mount(TodoItem, { props: { todo } });

      cy.get('[data-cy=edit-btn]').click();
      cy.get('[data-cy=edit-input]').clear().type('Some changes');
      cy.get('[data-cy=edit-cancel-btn]').click();

      cy.contains(todo.content).should('be.visible');
      cy.get('[data-cy=edit-input]').should('not.exist');
    });

    it('cancels edit and restores original content on escape button pressed', () => {
      cy.mount(TodoItem, { props: { todo } });

      cy.get('[data-cy=edit-btn]').click();
      cy.get('[data-cy=edit-input]').clear().type('Some changes{esc}');

      cy.contains(todo.content).should('be.visible');
      cy.get('[data-cy=edit-input]').should('not.exist');
    });

    it('does not emit edit if text is unchanged', () => {
      const onEdit = cy.spy().as('onEditSpy');

      cy.mount(TodoItem, {
        props: { todo },
        attrs: { onEdit },
      });

      cy.get('[data-cy=edit-btn]').click();
      cy.get('[data-cy=edit-input]').should('have.value', todo.content);
      cy.get('[data-cy=edit-save-btn]').click();

      cy.get('@onEditSpy').should('not.have.been.called');
    });

    it('cannot be saved when the new input is only whitespace (button disabled)', () => {
      const onEdit = cy.spy().as('onEditSpy');

      cy.mount(TodoItem, {
        props: { todo },
        attrs: { onEdit },
      });

      cy.get('[data-cy=edit-btn]').click();
      cy.get('[data-cy=edit-input]').clear().type('   ');
      cy.get('[data-cy=edit-save-btn]').should('be.disabled');
    });
  });
});
