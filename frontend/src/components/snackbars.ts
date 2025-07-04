import { Notify } from 'quasar';

export const showErrorSnackbar = (message: string): void => {
  Notify.create({
    message: message,
    position: 'bottom-right',
    color: 'red-10',
    textColor: 'white',
    icon: 'warning',
    timeout: 5_000,
    actions: [{ label: 'Dismiss', color: 'white' }],
  });
};
