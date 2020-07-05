import { IEmailTemplate } from "projects/ip-email-builder/src/lib/email-editor/iemail-template";
import { IDataSourceMeta } from "projects/ip-email-builder/src/lib/email-editor/data-source/Models/DataSourceMeta";

export interface IDataSource {
  id: number;
  name: string;
  emailTemplate: IEmailTemplate;
  sqlQuery: string;
  creation: string;
  metaList: IDataSourceMeta[];
  readOnlyQuery:boolean;
}