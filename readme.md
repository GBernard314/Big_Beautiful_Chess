# Big Beautiful Chess

---
![GitHub contributors](https://img.shields.io/github/contributors/Azarogue/BigBeautifulChess?color=orange) 
![GitHub code size in bytes](https://img.shields.io/github/languages/code-size/azarogue/BigBeautifulChess?color=blue) 

## Table of Contents

- **[What is it about ?](#What-is-it-about-?)**<br>
- **[What has been implemented yet](#What-as-been-implemented-yet)**<br>
- **[Use](#Use)**<br>
- **[Collaborators](#Collaborators)**<br>

## What is it about ?

**BBC** is ~~the British Broadcasting Corporation~~ **Big Beautiful Chess** the newest chess game, **ONLINE**, with friends list management which help playing chess with your friends all over the world.

This project was made in 4 days in a row, as a school subject given by <a href="https://github.com/sminet">Sebastien MINET</a> through the <a href="https://www.isen.fr/">ISEN</a> curriculum.

## What has been implemented yet

- [x] Game engine allowing you to play by the rules (almost all of them)
- [x] The possibility to check mate your mate !
- [x] Being able to call it a draw (by really tricky ways),
- [x] A nifty dashboard to see your stats,
- [x] A minimalist design for the website, allowing you to focus on what's important, to **challenge yourself**.

## Use

After using the provided .sql database (for easy & fast testability)

Default user : 

```
test / test123
```

### User Case :

To launch a game, as wanted, you have to have at least a friend. You can add one using : 

``` 
yourURL:8080/addFriend/username
```

(obviously, that one user needs to exist ... ! )

## Collaborators

<a href="https://github.com/PDesoomer">Pierre DESOOMER</a> :

- Managed the database stuff (Query, Management, HQL).
- Did the security of password with encryption with **Spring Security**.
- Worked out the appealing login/register page.

<a href="https://github.com/TibRib">Thibaud SIMON</a> :

- Worked on the controllers, the links between front end and back end.
- Did the template work on **ThymeLeaf** framework and fragments, along with CSS and JS.
- The one that did the majority of the controllers.

<a href="https://github.com/Jed13">Jeremie DUFOUR</a> :

- In charge of the translation of every move played in the game in the **S**hort **A**lgebraic **N**otation.

<a href="https://github.com/Azarogue">Guillaume BERNARD</a> :

- Did the game engine, the one to blame for not knowing the rules well enough.

- Also the one to acknowledge when everything is working smoothly especially weird moves.

  




