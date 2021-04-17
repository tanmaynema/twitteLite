//Twitter Lite

import java.util.*;
import java.lang.*;

public class twitter{

    // counter for the tweet number for NewsFeed
    static int tweetCount = 0;

    // user directory storing detials for all users
    HashMap <Integer, user> userDirectory;
    // tweet directory storing details about every tweet
    ArrayList<Integer> tweetDirectory = new ArrayList<Integer>();

    // Tweet Object (acts as a linked list)
    public class tweet{
        int tweetId;
        int tweetCounter;
        tweet nextInLine;

        //constructor to creat a new tweet
        public tweet(int tweetId){
            this.tweetId = tweetId;
            tweetCounter = tweetCount++;
            nextInLine = null;
        }
    }

    // User Object
    public class user{
        int userId;

        //list of followees
        public Set<Integer> followee;

        //tweetId pointer for the linked list
        public tweet header;
        
        //contructor to create a new user
        public user(int userId){
            this.userId = userId;
            followee = new HashSet<>();
            
            //follow self
            follow(userId);
            header = null;
        }

        //add self to followee list for news feed
        public void follow(int userId){
            followee.add(userId);
        }

        //remove self from followee list for news feed
        public void unfollow(int userId){
            followee.remove(userId);
        }

        //add self tweet to tweet header and change current to nextInLine 
        //Store tweet details to tweet directory
        public void post(int tweetId){
            tweetDirectory.add(tweetId);
            tweet tweetPost = new tweet(tweetId);
            tweetPost.nextInLine = header;
            header = tweetPost;
        }
    }

    // counstructor for twitter classs
    public twitter(){
        userDirectory  = new HashMap<Integer,user>();
    }

    //method to create a new user
    public user createUser(int userId){
        user newUser = new user(userId);
        //check if userId already exists and userId is positive integer
        if(userDirectory.containsKey(userId)){
            System.out.println("User already exisits. Please create new user.");
            
        } else if(userId>0){
            userDirectory.put(userId,newUser);
            System.out.println("User created succesfully");
        } else{
            System.out.println("Please enter valid userId.");
        }
        return newUser;

    }
    
    //method to post a tweet
    public void postTweet(int userId, int tweetId){
        //check if userId exists and tweetId is positive integer
        if(!userDirectory.containsKey(userId)){
            System.out.println("User doesn't exist. Please create user first.");
            return;
        } else if(tweetId<1){
            System.out.println("Please enter valid tweetId.");
            return;
        }
        //check if tweetId already exists
        if(tweetDirectory.contains(tweetId)){
            System.out.println("Tweet already exists. Please create new tweet.");
            return;
        }
        userDirectory.get(userId).post(tweetId);
        System.out.println("Tweet updated succesfully");
    }

    //method to follow a user
	public void follow(int followerId, int followeeId) {
        //check if userId exists for follower
		if(!userDirectory.containsKey(followerId)){
			System.out.println("Follower doesn't exist. Please create user first.");
            return;
        }
        //check if userId exists for followee
		if(!userDirectory.containsKey(followeeId)){
			System.out.println("Followee doesn't exist. Please create user first.");
            return;
        }
        //check if both userIds match 
        if(followeeId==followerId){
			System.out.println("Error! Cannot follow self.");
            return;
        }

        userDirectory.get(followerId).follow(followeeId);
        System.out.println("User followed succesfully");
	}

	//method to unfollow a user
	public void unfollow(int followerId, int followeeId) {
        //check if userId exists for follower
        if(!userDirectory.containsKey(followerId)){
			System.out.println("Follower doesn't exist. Please create user first.");
            return;
        }
        //check if userId exists for followee
		if(!userDirectory.containsKey(followeeId)){
			System.out.println("Followee doesn't exist. Please create user first.");
            return;
        }
        //check if both userIds match 
        if(followeeId==followerId){
			System.out.println("Error! Cannot follow self.");
            return;
        }
        userDirectory.get(followerId).unfollow(followeeId);
        System.out.println("User unfollowed succesfully");
    }
    
    //method to generate news feed for specific user with top 10 tweetIds
	public List<Integer> getNewsFeed(int userId) {

		List<Integer> feed = new LinkedList<>();
        
        //check if both userId exists 
        if(!userDirectory.containsKey(userId)){
            System.out.println("User doesn't exist. Please create user first.");
            return feed;
        }
        
        Set<Integer> users = userDirectory.get(userId).followee;
        //sorting the tweetIds based on tweetCounter
        PriorityQueue<tweet> allTweets = new PriorityQueue<tweet>(users.size(), (a,b)->(b.tweetCounter-a.tweetCounter));
        
		for(int eachUser: users){
			tweet t = userDirectory.get(eachUser).header;
			if(t!=null){
				allTweets.add(t);
			}
        }
        
		int n=0;
		while(!allTweets.isEmpty() && n<10){
		  tweet t = allTweets.poll();
		  feed.add(t.tweetId);
		  n++;
		  if(t.nextInLine!=null){
              allTweets.add(t.nextInLine);
            }
        }
        System.out.println("NewsFeed for user "+userId+": \n"+feed);
		return feed;
	}
    
    public static void main(String[] args){
            
        twitter obj = new twitter();
        Scanner input = new Scanner(System.in);
        int actionNumber=0;
        int userId;
        int tweetId;
        int followerId;
        int followeeId;

        System.out.println("Welcome to Twitter Lite!\n");

        do{
            System.out.println("\nActions: \n(1) Post a tweet\n(2) Get NewsFeed\n(3) Create a user\n(4) Follow\n(5) Unfollow \n(0) Exit");

            //reading user input
            System.out.println("\nPlease select action number: ");
            
            try{
                actionNumber = input.nextInt();
            } catch(InputMismatchException e){
                System.out.println("Please enter valid input.");
                return;
            }
               
            switch (actionNumber){
                case 0:{
                    //exit
                    System.out.println("Exiting...");
                    return;
                }
                            
                case 1:{
                    //Post a Tweet
                    System.out.println("Enter unique UserID: ");
                    try{
                        userId = input.nextInt();
                    } catch(InputMismatchException e){
                        System.out.println("Please enter valid input.");
                        return;
                    }
                    
                    System.out.println("Enter unique tweetID: ");
                    try{
                        tweetId = input.nextInt();
                    } catch(InputMismatchException e){
                        System.out.println("Please enter valid input.");
                        return;
                    }
                    obj.postTweet(userId,tweetId);
                    break;
                }

                case 2:{
                    //Get NewsFeed
                    System.out.println("Enter unique UserID: ");
                    try{
                        userId = input.nextInt();
                    } catch(InputMismatchException e){
                        System.out.println("Please enter valid input.");
                        return;
                    }
                    List<Integer> newsFeed = obj.getNewsFeed(userId);
                    break;
                }
                
                case 3:{
                    //Create a User
                    System.out.println("Enter unique UserID: ");
                    try{
                        userId = input.nextInt();
                    } catch(InputMismatchException e){
                        System.out.println("Please enter valid input.");
                        return;
                    }
                    obj.createUser(userId);
                    break;
                }
                
                case 4:{
                    //Follow
                    System.out.println("Enter unique UserID for Follower: ");
                    try{
                        followerId = input.nextInt();
                    } catch(InputMismatchException e){
                        System.out.println("Please enter valid input.");
                        return;
                    }
                                        
                    System.out.println("Enter unique userID for Followee: ");
                    try{
                        followeeId = input.nextInt();
                    } catch(InputMismatchException e){
                        System.out.println("Please enter valid input.");
                        return;
                    }
                    obj.follow(followerId,followeeId);
                    break;
                }
                

                case 5:{
                    //Unfollow
                    System.out.println("Enter unique UserID for Follower: ");
                    try{
                        followerId = input.nextInt();
                    } catch(InputMismatchException e){
                        System.out.println("Please enter valid input.");
                        return;
                    }
                    
                    System.out.println("Enter unique userID for Followee: ");
                    try{
                        followeeId = input.nextInt();
                    } catch(InputMismatchException e){
                        System.out.println("Please enter valid input.");
                        return;
                    }
                    obj.unfollow(followerId,followeeId);
                    break;
                }

                default: System.out.println("Unrecognized entry. Please recheck.");
            }

        }while(actionNumber!=0);
        input.close();
    }

}