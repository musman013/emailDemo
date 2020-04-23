export interface IApps {  

	artifactId: string;
	authMethod?: string;
	authTable?: string;
	caching?: boolean;
	createdDate?: Date;
	description?: string;
	destinationPath: string;
	emailTemplates?: boolean;
	entityHistory?: boolean;
	groupId: string;
	id: number;
	jdbcPassword: string;
	jdbcUrl: string;
	jdbcUsername: string;
	name?: string;
	processAdminApp?: boolean;
	processManagement?: boolean;
	scheduler?: boolean;
	schema?: string;
	upgrade?: boolean;
	userId?: number;
	userOnly?: boolean;
}
