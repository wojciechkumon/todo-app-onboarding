import { JSONSchemaType } from "ajv";
import { ajv } from "../validation/ajv";

export interface UpdateTodoRequest {
  content: string;
  completed: boolean;
}

const updateTodoSchema: JSONSchemaType<UpdateTodoRequest> = {
  type: "object",
  properties: {
    content: { type: "string", minLength: 1, maxLength: 255 },
    completed: { type: "boolean" },
  },
  required: ["content", "completed"],
  additionalProperties: false
};

export const validateUpdateTodo = ajv.compile(updateTodoSchema);
