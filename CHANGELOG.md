Changes that we have made in PA04:
1) We have changed the attack methods because in our initial code there were no methods 
from the player interface, so we added setup, takeShots, successfulHits, reportDamage methods
2) We have changed the way of how our AI shots. It does not shoot at the same position twice,
and it does not create two identical hits in the volley
3) We have changed the board because in our initial code board was reversed, so the server could
 not proceed the shots.
4) We have added the abstractPlayer to simplify the Controller and AiController class