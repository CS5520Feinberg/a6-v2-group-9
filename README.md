# Summer 2023 CS5520 Mobile App Development Group 9 Assignment App

Jialin Huang, Kevin Lin, Akshat Alkesh Gandhi, Vaibhav Garg, Shashank Manjunath

## Backend Notes

- LoginActivity Class (@smanjunath)
  - getUser() -- Returns current FirebaseUser. Returns null if no user is set
    yet
  - registerOrAuthUser(String user_email) -- Registers a user with the given
    email, or authenticates if they already exist. Also sets the current user
    in LoginActivity as the registered user (i.e. no need to call
    authenticateUser after calling registerUser). Returns void, to get the user
    use the getUser() method.
  - authenticateUser(String user_email) -- Authenticates user. Sets the current
    user in LoginActivity as the registered user. Returns void, to get the user
    use the getUser() method.

#### Packages - @AkshatGM6

- apis -- all the apis

    - MainApi class -- To get total messages in conversation between user A and user B.
        - getCombinedMessages -- Returns the List
        
        
- dbmanager -- Base functions related to the Firebase

- interfaces -- Provides callback functions to help with async methods 

- modals -- Base structure for DB elements

#### Class - @AkshatGM6
- StickerActivty.


