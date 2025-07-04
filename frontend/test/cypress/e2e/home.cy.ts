import { Todo } from 'src/api/todos';

describe('Todo List App', () => {
  beforeEach(() => {
    cy.intercept('GET', '**/todos', { statusCode: 200, body: [] }).as('listTodos');
    cy.intercept('DELETE', '**/todos/*', { statusCode: 204 }).as('deleteTodo');
    cy.visit('/');
  });

  it('should assert that <title> is correct', () => {
    cy.title().should('include', 'Todo list');
  });

  const createTodo = (
    todoText: string,
    { skipNetworkMock }: { skipNetworkMock?: boolean } = {},
  ): Todo => {
    const todo: Todo = {
      id: todoText,
      content: todoText,
      completed: false,
      createdAt: new Date().toISOString(),
    };
    if (!skipNetworkMock) {
      cy.intercept('POST', '**/todos', { statusCode: 201, body: todo }).as('createTodo');
    }
    cy.get('textarea[aria-label="New todo item"]').type(todoText);
    cy.contains('button', 'Add').click();
    cy.wait('@createTodo');
    return todo;
  };

  it('should list todos from the endpoint on load', () => {
    cy.intercept('GET', '**/todos', {
      statusCode: 200,
      body: [createTodo('first todo'), createTodo('second todo')],
    }).as('listTodos');

    cy.contains('first todo').should('be.visible');
    cy.contains('second todo').should('be.visible');
    cy.get('[data-cy=todo-item]').should('be.visible');
  });

  it('should add a todo item', () => {
    const todoText = 'My first todo';
    createTodo(todoText);

    cy.contains(todoText).should('be.visible');
    cy.get('[data-cy=todo-item]').should('be.visible');
  });

  it('should add 5 todo items', () => {
    const numberOfTodos = 3;
    for (let i = 1; i <= numberOfTodos; i++) {
      const todoText = `Todo item ${i}`;
      createTodo(todoText);
    }
    for (let i = 1; i <= numberOfTodos; i++) {
      cy.contains(`Todo item ${i}`).should('be.visible');
    }

    cy.get('[data-cy=todo-item]').should('have.length', numberOfTodos);
  });

  it('should show an error snackbar on todo item creation error (HTTP 500)', () => {
    cy.intercept('POST', '**/todos', { statusCode: 500, body: 'error' }).as('createTodo');
    const todoText = 'My first todo';
    createTodo(todoText, { skipNetworkMock: true });

    cy.contains('Error while creating a todo, please try again later').should('be.visible');
    cy.get('[data-cy=todo-item]').should('not.exist');
  });

  it('should add 3 todos and delete the middle one', () => {
    const todos = ['First todo', 'Second todo', 'Third todo'];
    todos.forEach((todo) => createTodo(todo));
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

  describe('editing', () => {
    it('should edit the middle todo item', () => {
      const todosText = ['First todo', 'Second todo', 'Third todo'];
      const todos = todosText.map((todo) => createTodo(todo));
      cy.get('[data-cy=todo-item]').should('have.length', 3);

      const newContent = 'todo edited!';

      cy.intercept('PUT', '**/todos/*', {
        statusCode: 200,
        body: {
          id: todos[1]!.id,
          content: newContent,
          completed: todos[1]!.completed,
          createdAt: todos[1]!.createdAt,
        } satisfies Todo,
      }).as('updateTodo');
      cy.get('[data-cy=todo-item]')
        .eq(1)
        .within(() => {
          cy.get('[data-cy=edit-btn]').click();
          cy.get('[data-cy=edit-input]').clear().type(newContent);
          cy.get('[data-cy=edit-save-btn]').click();
        });

      cy.contains('First todo').should('be.visible');
      cy.contains('Second todo').should('not.exist');
      cy.contains('todo edited!').should('be.visible');
      cy.contains('Third todo').should('be.visible');
    });

    it('should cancel editing the middle todo item', () => {
      const todos = ['First todo', 'Second todo', 'Third todo'];
      todos.forEach((todo) => createTodo(todo));
      cy.get('[data-cy=todo-item]').should('have.length', 3);

      cy.get('[data-cy=todo-item]')
        .eq(1)
        .within(() => {
          cy.get('[data-cy=edit-btn]').click();
          cy.get('[data-cy=edit-input]').clear().type('new todo text that will be cancelled');
          cy.get('[data-cy=edit-cancel-btn]').click();
        });

      cy.contains('First todo').should('be.visible');
      cy.contains('Second todo').should('be.visible');
      cy.contains('new todo text that will be cancelled').should('not.exist');
      cy.contains('Third todo').should('be.visible');
    });
  });

  it('should complete and revert a todo item', () => {
    const todoText = 'new item';
    const todo = createTodo(todoText);

    // mark as completed
    cy.intercept('PUT', '**/todos/*', {
      statusCode: 200,
      body: {
        id: todo.id,
        content: todo.content,
        completed: true,
        createdAt: todo.createdAt,
      } satisfies Todo,
    }).as('updateTodo');
    cy.get('[data-cy=todo-item]')
      .first()
      .within(() => {
        cy.get('[data-cy=todo-checkbox]').click();
      });

    cy.get('.q-badge').contains('completed').should('be.visible');
    cy.get('[data-cy=completed-todo-item]').should('be.visible');

    // revert completion
    cy.intercept('PUT', '**/todos/*', {
      statusCode: 200,
      body: {
        id: todo.id,
        content: todo.content,
        completed: false,
        createdAt: todo.createdAt,
      } satisfies Todo,
    }).as('updateTodo');
    cy.get('[data-cy=completed-todo-item]')
      .first()
      .within(() => {
        cy.get('[data-cy=todo-checkbox]').click();
      });

    cy.get('.q-badge').should('not.exist');
    cy.get('[data-cy=completed-todo-item]').should('not.exist');
  });
});

// Workaround for Cypress AE + TS + Vite
// See: https://github.com/quasarframework/quasar-testing/issues/262#issuecomment-1154127497
export {};
