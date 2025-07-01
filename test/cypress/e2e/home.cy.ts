describe('Todo List App', () => {
  beforeEach(() => {
    cy.visit('/');
  });

  it('should assert that <title> is correct', () => {
    cy.title().should('include', 'Todo list');
  });

  it('should add a todo item', () => {
    const todoText = 'My first todo';

    cy.get('input[aria-label="New todo item"]').type(todoText);
    cy.contains('button', 'Add').click();

    cy.contains(todoText).should('be.visible');
  });

  it('should add 10 todo items', () => {
    const numberOfTodos = 10;
    for (let i = 1; i <= numberOfTodos; i++) {
      const todoText = `Todo item ${i}`;
      cy.get('input[aria-label="New todo item"]').type(todoText);
      cy.contains('button', 'Add').click();
    }
    for (let i = 1; i <= numberOfTodos; i++) {
      cy.contains(`Todo item ${i}`).should('be.visible');
    }

    cy.get('[data-cy=todo-item]').should('have.length', numberOfTodos);
  });
});

// Workaround for Cypress AE + TS + Vite
// See: https://github.com/quasarframework/quasar-testing/issues/262#issuecomment-1154127497
export {};
