# Summer 2023 CS5520 Mobile App Development Group 9 Assignment App

Jialin Huang, Kevin Lin, Akshat Alkesh Gandhi, Vaibhav Garg, Shashank Manjunath

## Backend Notes

- LoginActivity Class (@smanjunath)
  - getUser() -- Returns current FirebaseUser. Returns null if no user is set
    yet
  - registerUser(String user_email) -- Registers a user with the given email.
    Also sets the current user in LoginActivity as the registered user (i.e.
    no need to call authenticateUser after calling registerUser). Returns void,
    to get the user use the getUser() method.
  - authenticateUser(String user_email) -- Authenticates user. Sets the current
    user in LoginActivity as the registered user. Returns void, to get the user
    use the getUser() method.
