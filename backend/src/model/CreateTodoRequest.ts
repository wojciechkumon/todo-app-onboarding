import { JSONSchemaType } from "ajv";
import { ajv } from "../validation/ajv";

export interface CreateTodoRequest {
  content: string;
}

const createTodoSchema: JSONSchemaType<CreateTodoRequest> = {
  type: "object",
  properties: {
    content: { type: "string", minLength: 1, maxLength: 255 }
  },
  required: ["content"],
  additionalProperties: false
};

export const validateCreateTodo = ajv.compile(createTodoSchema);
