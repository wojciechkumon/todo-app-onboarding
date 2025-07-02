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

describe('completed todo feature', () => {
  it('should render an uncompleted todo', () => {
    const uncompletedTodo: Todo = { id: 1, content: 'New todo', completed: false };

    cy.mount(TodoItem, { props: { todo: uncompletedTodo } });

    cy.get('[data-cy=todo-checkbox]').should('have.attr', 'aria-checked', 'false');
    cy.contains('completed').should('not.exist');
    cy.get('.q-item').should('not.have.class', 'completed-todo');
  });

  it('should render a completed todo', () => {
    const completedTodo: Todo = { id: 1, content: 'Finished todo', completed: true };

    cy.mount(TodoItem, { props: { todo: completedTodo } });

    cy.get('[data-cy=todo-checkbox]').should('have.attr', 'aria-checked', 'true');
    cy.contains('completed').should('be.visible');
    cy.get('.q-item').should('have.class', 'completed-todo');
  });

  it('emits toggle-complete event when checkbox is clicked', () => {
    const onToggleComplete = cy.spy().as('onToggleCompleteSpy');
    const todo: Todo = { id: 1, content: 'Test todo', completed: false };

    cy.mount(TodoItem, {
      props: { todo },
      attrs: { 'onToggle-complete': onToggleComplete },
    });

    cy.get('[data-cy=todo-checkbox]').click();
    cy.get('@onToggleCompleteSpy').should('have.been.calledOnce');
  });
});
