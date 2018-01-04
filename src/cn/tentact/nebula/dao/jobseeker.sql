#sql("login")
SELECT COUNT(*)
FROM jobseeker
WHERE
username=? AND 
password=HEX(AES_ENCRYPT(?,"nebula"));
#end
#sql("search")
SELECT *
FROM jobseeker
WHERE
username=? AND 
password=HEX(AES_ENCRYPT(?,"nebula"));
#end
#sql("check")
SELECT COUNT(*)
FROM jobseeker
WHERE
username=? ;
#end
#sql("register")
INSERT INTO jobseeker(id,username,password,exp,type,authentication)
VALUES (
	NEXT VALUE FOR MYCATSEQ_JOBSEEKER,
	?,
	HEX(AES_ENCRYPT(?,"nebula")),
	0,
	"普通用户",
	false
);
#end
#sql("updat")
UPDATE resume
set sex=?,birthday=?,height=?,weight=?,marriage=?,ancestral_home=?,tel=?,email=?,education
=?;
#end
#sql("resume")
SELECT * from resume
where name=?;
#end


