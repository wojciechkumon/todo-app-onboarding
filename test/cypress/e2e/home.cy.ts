describe('Todo List App', () => {
  beforeEach(() => {
    cy.visit('/');
  });

  it('should assert that <title> is correct', () => {
    cy.title().should('include', 'Todo list');
  });

  it('should add a todo item', () => {
    const todoText = 'My first todo';

    cy.get('textarea[aria-label="New todo item"]').type(todoText);
    cy.contains('button', 'Add').click();

    cy.contains(todoText).should('be.visible');
  });

  it('should add 5 todo items', () => {
    const numberOfTodos = 5;
    for (let i = 1; i <= numberOfTodos; i++) {
      const todoText = `Todo item ${i}`;
      cy.get('textarea[aria-label="New todo item"]').type(todoText);
      cy.contains('button', 'Add').click();
    }
    for (let i = 1; i <= numberOfTodos; i++) {
      cy.contains(`Todo item ${i}`).should('be.visible');
    }

    cy.get('[data-cy=todo-item]').should('have.length', numberOfTodos);
  });

  it('should add 3 todos and delete the middle one', () => {
    const todos = ['First todo', 'Second todo', 'Third todo'];
    todos.forEach((todo) => {
      cy.get('textarea[aria-label="New todo item"]').type(todo);
      cy.contains('button', 'Add').click();
    });
    cy.get('[data-cy=todo-item]').should('have.length', 3);

    cy.get('[data-cy=todo-item]')
      .eq(1)
      .within(() => {
        cy.get('[data-cy=delete-btn]').click();
      });

    cy.get('[data-cy=todo-item]').should('have.length', 2);
    cy.contains('First todo').should('be.visible');
    cy.contains('Second todo').should('not.exist');
    cy.contains('Third todo').should('be.visible');
  });
});

// Workaround for Cypress AE + TS + Vite
// See: https://github.com/quasarframework/quasar-testing/issues/262#issuecomment-1154127497
export {};
