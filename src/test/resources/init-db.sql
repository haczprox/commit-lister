INSERT INTO repository ("name") VALUES ('test/TestRepo');

INSERT INTO public."commit" (id,author,"date",message,sha,repository_name) VALUES
(9999,'John Doe <john.test@test.com>','2021-07-29 17:32:16.000',' Latest commit. ','8e64b0650e0066ac1b73299affac3facd182a988','test/TestRepo')
,(9998,'Jane Doe <john.test@test.com>','2021-07-29 16:55:27.000',' Third commit. ','974a35e265597d5c35af7dc2595c95a0dc0162f1','test/TestRepo')
,(9997,'John Doe <john.test@test.com>','2021-07-29 02:53:17.000',' Second commit. ','30519d4c50d3d04432c8769d78ef8a16bd99daa0','test/TestRepo')
,(9996,'Jane Doe <john.test@test.com>','2021-07-26 11:10:13.000',' Initial commit. ','c8bbdff98528783098d99718eb3ef14813615ba9','test/TestRepo')
;