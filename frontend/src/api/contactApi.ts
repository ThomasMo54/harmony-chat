import apiClient from "./client.ts";
import { Page } from "./types/pagination.ts";

export const CONTACT_ERROR_CODES = {
  REQUEST_ALREADY_SENT: "error.request.already_sent",
  ALREADY_IN_CONTACT: "error.request.already_in_contact",
  REQUEST_TO_SELF: "error.request.self",
  REQUEST_NOT_FOUND: "error.request.not_found",
} as const;

export class CreateContactRequestDto {
  requestedId!: string;
}

export class ContactRequestAnswerDto {
  requestedId!: string;
}

export class ContactRequestDto {
  requesterId!: string;
  requestedId!: string;
  createdAt!: string;
}

export class ContactSummaryDto {
  userId!: string;
  userName!: string;
  date!: string;
}

export async function requestContact(createContactRequestDto: CreateContactRequestDto) {
  await apiClient.post('/contacts/requests', createContactRequestDto);
}

export async function findContacts(page: number, size: number) {
  const { data } = await apiClient.get<Page<ContactSummaryDto>>('/contacts', {
    params: { page, size },
  });
  return data;
}

export async function findSentContactRequests(page: number, size: number) {
  const { data } = await apiClient.get<Page<ContactRequestDto>>('/contacts/requests/sent', {
    params: { page, size },
  });
  return data;
}

export async function findReceivedContactRequests(page: number, size: number) {
  const { data } = await apiClient.get<Page<ContactRequestDto>>('/contacts/requests/received', {
    params: { page, size },
  });
  return data;
}

export async function acceptContactRequest(contactRequestAnswerDto: ContactRequestAnswerDto) {
  await apiClient.put('/contacts/requests/accept', contactRequestAnswerDto);
}

export async function rejectContactRequest(contactRequestAnswerDto: ContactRequestAnswerDto) {
  await apiClient.put('/contacts/requests/reject', contactRequestAnswerDto);
}