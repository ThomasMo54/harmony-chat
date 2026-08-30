import apiClient from './client.ts';

export const USER_ERROR_CODES = {
  EMAIL_ALREADY_EXISTS: 'error.user.email_already_exists',
  NAME_ALREADY_EXISTS: 'error.user.name_already_exists',
  NOT_FOUND: 'error.user.not_found',
  ACTIVATION_CODE_NOT_FOUND: 'error.user.code.not_found',
  ACTIVATION_CODE_EXPIRED: 'error.user.code.expired',
  ALREADY_ACTIVATED: 'error.user.already_activated',
} as const;

export class CreateUserDto {
  email!: string;
  password!: string;
  name!: string;
}

export class UserDto {
  id!: string;
  email!: string;
  name!: string;
}

export class ActivateUserDto {
  code!: string;
}

export class ResendCodeDto {
  userId!: string;
}

export async function createUser(createUserDto: CreateUserDto) {
  const { data } = await apiClient.post('/users', createUserDto);
  return data as UserDto;
}

export async function getMyUser() {
  const { data } = await apiClient.get('/users/me');
  return data as UserDto;
}

export async function resendCode(resendCodeDto: ResendCodeDto) {
  await apiClient.post('/users/resend-code', resendCodeDto);
}

export async function activateUser(activateUserDto: ActivateUserDto) {
  await apiClient.patch('/users/activate', activateUserDto);
}
