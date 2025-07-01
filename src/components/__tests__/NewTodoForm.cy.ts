import NewTodoForm from '../NewTodoForm.vue';

describe('NewTodoForm', () => {
  it('should have a disabled button when the input is empty or with spaces only', () => {
    const addTodoSpy = cy.spy().as('addTodoSpy');

    cy.mount(NewTodoForm, { attrs: { onAddTodo: addTodoSpy } });

    cy.get('button').should('be.disabled');
    cy.get('input').type('   ').should('have.value', '   ');
    cy.get('button').should('be.disabled');
  });

  it('emits add-todo on button click', () => {
    const addTodoSpy = cy.spy().as('addTodoSpy');

    cy.mount(NewTodoForm, { attrs: { onAddTodo: addTodoSpy } });

    cy.get('input').type('Another task');
    cy.get('button').click();
    cy.get('@addTodoSpy').should('have.been.calledOnceWith', 'Another task');
    cy.get('input').should('have.value', '');
  });

  it('emits add-todo on Enter key', () => {
    const addTodoSpy = cy.spy().as('addTodoSpy');

    cy.mount(NewTodoForm, { attrs: { onAddTodo: addTodoSpy } });

    cy.get('input').type('Another task{enter}');
    cy.get('@addTodoSpy').should('have.been.calledOnceWith', 'Another task');
    cy.get('input').should('have.value', '');
  });

  it('should focus input on mount', () => {
    cy.mount(NewTodoForm, { attrs: { onAddTodo: () => undefined } });
    cy.get('input').should('be.focused')
  })
});
