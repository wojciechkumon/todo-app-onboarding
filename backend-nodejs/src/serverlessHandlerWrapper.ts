import serverless from 'serverless-http';
import { app } from "./router";

export const handler = serverless(app);
