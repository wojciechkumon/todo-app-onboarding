import NewTodoForm from '../NewTodoForm.vue';

describe('NewTodoForm', () => {
  it('should have a disabled button when the textarea is empty or with spaces only', () => {
    const addTodoSpy = cy.spy().as('addTodoSpy');

    cy.mount(NewTodoForm, { attrs: { onAddTodo: addTodoSpy } });

    cy.get('button').should('be.disabled');
    cy.get('textarea').type('   ').should('have.value', '   ');
    cy.get('button').should('be.disabled');
  });

  it('emits add-todo on button click', () => {
    const addTodoSpy = cy.spy().as('addTodoSpy');

    cy.mount(NewTodoForm, { attrs: { onAddTodo: addTodoSpy } });

    cy.get('textarea').type('Another task');
    cy.get('button').click();
    cy.get('@addTodoSpy').should('have.been.calledOnceWith', 'Another task');
    cy.get('textarea').should('have.value', '');
  });

  it('should focus textarea on mount', () => {
    cy.mount(NewTodoForm, { attrs: { onAddTodo: () => undefined } });
    cy.get('textarea').should('be.focused');
  });
});
