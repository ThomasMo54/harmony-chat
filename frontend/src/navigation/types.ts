export type AuthStackParamList = {
  Login: undefined;
  Register: undefined;
  Verify: { email: string; userId: string };
};

export type ContactStackParamList = {
  ContactList: undefined;
  ContactRequestList: undefined;
};
