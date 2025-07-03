import type { AWS } from '@serverless/typescript';
import { region, todosTableName } from "./serverless.resource";

const serverlessConfiguration: AWS = {
  org: 'voytekorg',
  app: 'todo-list',
  service: 'todo-app-backend',
  frameworkVersion: '4',
  provider: {
    name: 'aws',
    runtime: 'nodejs22.x',
    region,
    iamRoleStatements: [
      {
        Effect: 'Allow',
        Action: [
          'dynamodb:PutItem',
          'dynamodb:GetItem',
          'dynamodb:UpdateItem',
          'dynamodb:DeleteItem',
          'dynamodb:Scan',
        ],
        Resource: [{ 'Fn::GetAtt': ['TodosTable', 'Arn'] }],
      },
    ],
  },
  functions: {
    api: {
      handler: 'src/serverlessHandlerWrapper.handler',
      events: [
        {
          httpApi: '*',
        },
      ],
    },
  },
  resources: {
    Resources: {
      TodosTable: {
        Type: 'AWS::DynamoDB::Table',
        Properties: {
          TableName: todosTableName,
          BillingMode: "PAY_PER_REQUEST",
          AttributeDefinitions: [
            { AttributeName: 'id', AttributeType: 'S' },
          ],
          KeySchema: [
            { AttributeName: 'id', KeyType: 'HASH' },
          ],
        },
      },
    }
  }
};

module.exports = serverlessConfiguration;
