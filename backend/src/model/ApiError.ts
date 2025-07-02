import { ErrorObject } from "ajv";

export interface ApiError {
  message: string;
  errors?: ErrorObject[];
}
