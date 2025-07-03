import { DynamoDBClient } from '@aws-sdk/client-dynamodb';
import { DeleteCommand, DynamoDBDocumentClient, PutCommand, ScanCommand, UpdateCommand } from '@aws-sdk/lib-dynamodb';
import { region, todosTableName } from "../../serverless.resource";
import { Todo } from "../model/Todo";
import { UpdateTodoRequest } from "../model/UpdateTodoRequest";

export class TodosRepository {
  private static readonly client = new DynamoDBClient({ region });
  private static readonly ddbDocClient = DynamoDBDocumentClient.from(this.client);

  public static async create(todo: Todo): Promise<void> {
    const command = new PutCommand({
      TableName: todosTableName,
      Item: todo,
    });
    await this.ddbDocClient.send(command);
  }

  public static async listAll(): Promise<Todo[]> {
    const command = new ScanCommand({ TableName: todosTableName });
    const result = await this.ddbDocClient.send(command);
    return result.Items as Todo[] || [];
  }

  public static async delete(id: string): Promise<void> {
    const command = new DeleteCommand({
      TableName: todosTableName,
      Key: { id },
    });
    await this.ddbDocClient.send(command);
  }

  public static async update(id: string, newValues: UpdateTodoRequest): Promise<Todo | null> {
    const command = new UpdateCommand({
      TableName: todosTableName,
      Key: { id },
      UpdateExpression: 'SET content = :content, completed = :completed',
      ExpressionAttributeValues: {
        ':content': newValues.content,
        ':completed': newValues.completed,
      },
      ConditionExpression: 'attribute_exists(id)',
      ReturnValues: 'ALL_NEW',
    });
    try {
      const result = await this.ddbDocClient.send(command);
      return result.Attributes as Todo;
    } catch (err: any) {
      if (err.name === 'ConditionalCheckFailedException') {
        return null;
      }
      throw err;
    }
  }
}
