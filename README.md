# LMS

Library Management System REST API developed in Spring Boot, Hibernate and Oracle.

Use Case :
A Library consists of Library Sections (eg.: Children, General, Magazine).
Books belong to one of these Library Sections.
Books consist of Book Titles and Book Copies; a Book Title has one or more Book Copies of that title.
A Book is uniquely identified by its titleId and copyId (composite key).
Library has Subscription Packages which allow Members to issue books as per the privilege granted by that Subscription Package.
A Subscription Package provides access to one or more Library Sections and allows one or more Books of that Library Section to be issued simultaneously.
Subscription Package has monthly Subscription Fee as per its privileges.
A Member enrolls by paying refundable Deposite amount.
Enrolled Member can subscribe to the Subscription Package by paying its Subscription Fee.
Member can issue Books as per his Subscription.
Late Fee is charged in case of late return of Book.
