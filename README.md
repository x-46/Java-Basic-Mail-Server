# Java-Basic-Mail-Server

This is a mail server written in 100% Java without any libraries (the one library that is used is code that I outsourced because it became useful for other projects). 



The following functions are supported: 
## SMTP
- Authefication via LOGIN (username and passworden are encoded base64).
- Sending mails to any mail servers (are picked automatically). 
- cc is supported 
- tls encryption supported
- stores outgoing and incoming mails using the manger specified below 


## POP3 
- Authefication via PLAIN (username and passwords are overridden).
- tls encryption 
- sending saved emails  
- list of mails
(Compatible with any pop3 mail client)

## storage

There is a mail storage management which allows to store incoming and outgoing mails (including attachments) using a very simple interface. The saved emails are stored as files in different folders. 

## authentication
A very simple authentication from a MySQL DB (but can be customized very easily).


## The Libery 
https://github.com/x-46/JavaBaseServer

## Discalmer: 
The content and code in this project are provided "as is" without warranty, and the user assumes all risk and responsibility for using it. The authors make no warranties about accuracy, completeness, or suitability and disclaim liability for any damages resulting from use of the content and code. By using this project, the user agrees to these terms.