export type AuthStackParamList = {
  Login: undefined;
  Register: undefined;
  Verify: { email: string, userId: string };
};