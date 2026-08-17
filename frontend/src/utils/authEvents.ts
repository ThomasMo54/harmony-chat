type Listener = () => void;
const listeners: Listener[] = [];

export const authEvents = {
  onForceLogout(callback: Listener) {
    listeners.push(callback);
  },
  emitForceLogout() {
    listeners.forEach((cb) => cb());
  },
};