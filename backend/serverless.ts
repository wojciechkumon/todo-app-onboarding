import type { AWS } from '@serverless/typescript';

const serverlessConfiguration: AWS = {
  org: 'voytekorg',
  app: 'todo-list',
  service: 'todo-app-backend',
  frameworkVersion: '4',
  provider: {
    name: 'aws',
    runtime: 'nodejs22.x',
  },
  functions: {
    api: {
      handler: 'handler.handler',
      events: [
        {
          httpApi: '*',
        },
      ],
    },
  },
};

module.exports = serverlessConfiguration;
