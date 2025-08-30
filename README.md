<h1>ResearchLNK's API designed in Java SpringBoot</h1> 


<h2>Purpose</h2>
<pre>
- Act as a full REST API for ResearchLNK developed in Springboot
- Allow all computation to be completed server-side
- Interface with MYSQL database, leaving all database operations server-side
</pre>


<h2>UML diagram</h2>
<img width="792" height="454" alt="image" src="https://github.com/user-attachments/assets/1c00039c-9578-44aa-8f22-0b48a212b6cd" />

<h2>Explanation</h2>

<h3>USER and PROFILE</h3> 
<pre>
- This is a one to one relationship: each user must have exactly one profile
- User is primarily used for authentication details, most functionality of the frontend will depend on the PROFILE
</pre>

<h3>PROFILE to PROFILE (Connections functionality)</h3>
<pre>
- Many profiles can be connected to many profiles (many to many relationship)
- A connection is the PROFILE object, it signifies a relationship between two profiles
- A connection can be labelled as PENDING or ACCEPTED depending on it's state
</pre>

<h3>PROFILE and POST</h3>
<pre>
- Each profile can have many posts
- Posts can be either available to CONNECTIONS ONLY or to PUBLIC
</pre>

<h3>PROFILE and SWIPECARD</h3>
<pre>
- Each profile has exactly one Swipecard
- This is a one to one relationship
</pre>

<h3>PROFILE and PROFILEEXPERIENCE</h3>
<pre>
- Each profile can have 0 to many PROFILEEXPERIENCES
- These are experiences that show up on a profile, that a user can optionally add if they believe it to be relevant
</pre>

<h3>SWIPECARD and RELEVANTEXPERIENCE</h3>
<pre>
- Each SWIPECARD can have 0 to 3 RELEVANTEXPERIENCES
- These relevant expereiences are displayed on a user's swipecard
</pre>


<h2>Authentication Details</h2>
- ORCID + OAUTH2.0 capabilities.

<h3>Frontend Ideal Authentication Workflow</h3>

ResearchLNK/Landing:
<pre>
- A user will enter their email on this page
- If the email exists in our database, the user is redirected to ResearchLNK/login
- If the email does not exist in our database, the user is redirected ot ResearchLNK/signup
</pre>

ResearchLNK/login:
<pre>
- API call to api/auth/login
- receives x-www-form-urlencoded data of email and password
- or, redirect to ORCID login at {baseURL}/oauth2/authorization/orcid?email={USEREMAIL}
  - This kickstarts the ORCID login process
</pre>

ResearchLNK/signup:
<pre>
- API call to api/users/signup/password
  - Receives JSON data for email and password
- or, redirect to ORCID login at {baseURL}/oauth2/authorization/orcid?email={USEREMAIL}
</pre>


<h2>NOTABLE implementation details</h2>
<pre>
- Any viewing of a user's public data (via user interactions) should be accompanied by a profile's publicId
- PublicId is how the frontend recognizes a different user's page
  - For example, my profile information will be rendered at ResearchLNK/profile/me
  - Anyone else's profile information will be rendered at ResearchLNK/profile/[publicID]
</pre>
 
  
  



