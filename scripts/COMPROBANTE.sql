/* Para evitar posibles problemas de pérdida de datos, debe revisar este script detalladamente antes de ejecutarlo fuera del contexto del diseñador de base de datos.*/
BEGIN TRANSACTION
SET QUOTED_IDENTIFIER ON
SET ARITHABORT ON
SET NUMERIC_ROUNDABORT OFF
SET CONCAT_NULL_YIELDS_NULL ON
SET ANSI_NULLS ON
SET ANSI_PADDING ON
SET ANSI_WARNINGS ON
COMMIT
BEGIN TRANSACTION
GO
CREATE TABLE dbo.Tmp_COMPROBANTE
	(
	SDDOCO float(53) NULL,
	SDDCTO nvarchar(255) NULL,
	SDLNID float(53) NULL,
	SDMCU float(53) NULL,
	SDCO float(53) NOT NULL,
	SDAN8 float(53) NOT NULL,
	ABALPH nvarchar(255) NULL,
	ABAN81 float(53) NULL,
	ALADDZ float(53) NULL,
	ALCTY1 nvarchar(255) NULL,
	ALCTR nvarchar(255) NULL,
	SDIVD float(53) NULL,
	SDVR01 nvarchar(255) NULL,
	SDDSC1 nvarchar(255) NULL,
	SDUORG float(53) NULL,
	SDBCRC nvarchar(255) NULL,
	SDUPRC float(53) NOT NULL,
	SDAEXP float(53) NULL,
	SDDOC float(53) NOT NULL,
	SDDCT float(53) NOT NULL,
	SDTXA1 nvarchar(255) NULL,
	SHDEL1 nvarchar(255) NULL,
	JMLINS float(53) NOT NULL,
	JMTXLN nvarchar(255) NULL,
	Q3$BCC nvarchar(255) NOT NULL,
	ABAC12 nvarchar(255) NULL,
	ABTAX float(53) NULL,
	ABTX2 nvarchar(255) NULL,
	SDPTC nvarchar(255) NULL,
	SDTORG nvarchar(255) NULL,
	SDTDAY float(53) NULL,
	JMPNTC nvarchar(255) NULL,
	SDKCOO float(53) NULL,
	SDCRCD nvarchar(255) NULL,
	Moneda nvarchar(255) NULL,
	SDCRR float(53) NULL,
	SDFUP float(53) NULL,
	SDFEA float(53) NULL,
	SDUOM nvarchar(255) NULL,
	SDUOM01 nvarchar(255) NULL,
	ALADD1 nvarchar(255) NULL,
	ALADD2 nvarchar(255) NULL,
	ALADD3 nvarchar(255) NULL,
	ALADD4 nvarchar(255) NULL,
	SDODOC float(53) NULL,
	SDODCT nvarchar(255) NULL
	)  ON [PRIMARY]
GO
ALTER TABLE dbo.Tmp_COMPROBANTE SET (LOCK_ESCALATION = TABLE)
GO
IF EXISTS(SELECT * FROM dbo.COMPROBANTE)
	 EXEC('INSERT INTO dbo.Tmp_COMPROBANTE (SDDOCO, SDDCTO, SDLNID, SDMCU, SDCO, SDAN8, ABALPH, ABAN81, ALADDZ, ALCTY1, ALCTR, SDIVD, SDVR01, SDDSC1, SDUORG, SDBCRC, SDUPRC, SDAEXP, SDDOC, SDDCT, SDTXA1, SHDEL1, JMLINS, JMTXLN, Q3$BCC, ABAC12, ABTAX, ABTX2, SDPTC, SDTORG, SDTDAY, JMPNTC, SDKCOO, SDCRCD, Moneda, SDCRR, SDFUP, SDFEA, SDUOM, SDUOM01, ALADD1, ALADD2, ALADD3, ALADD4, SDODOC, SDODCT)
		SELECT SDDOCO, SDDCTO, SDLNID, SDMCU, SDCO, SDAN8, ABALPH, ABAN81, ALADDZ, ALCTY1, ALCTR, SDIVD, SDVR01, SDDSC1, SDUORG, SDBCRC, SDUPRC, SDAEXP, SDDOC, SDDCT, SDTXA1, SHDEL1, JMLINS, JMTXLN, Q3$BCC, ABAC12, ABTAX, ABTX2, SDPTC, SDTORG, SDTDAY, JMPNTC, SDKCOO, SDCRCD, Moneda, SDCRR, SDFUP, SDFEA, SDUOM, SDUOM01, ALADD1, ALADD2, ALADD3, ALADD4, SDODOC, SDODCT FROM dbo.COMPROBANTE WITH (HOLDLOCK TABLOCKX)')
GO
DROP TABLE dbo.COMPROBANTE
GO
EXECUTE sp_rename N'dbo.Tmp_COMPROBANTE', N'COMPROBANTE', 'OBJECT' 
GO
ALTER TABLE dbo.COMPROBANTE ADD CONSTRAINT
	PK_COMPROBANTE PRIMARY KEY CLUSTERED 
	(
	SDCO,
	[Q3$BCC],
	SDDOC,
	JMLINS,
	SDDCT,
	SDAN8,
	SDUPRC
	) WITH( STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]

GO
COMMIT
