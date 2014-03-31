//
//  MainViewController.m
//  NowPlaying
//
//  Created by Kyle Buchanan on 12/30/13.
//  Copyright (c) 2013 Kyle Buchanan. All rights reserved.
//

#import "MainViewController.h"
#import <Parse/Parse.h>

@interface MainViewController ()

@property UITapGestureRecognizer *singleTap;

@end

@implementation MainViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    
    // log the screen in google analytics
    self.screenName = @"Main Screen";
    
    // Create page view controller
    self.pageViewController = [self.storyboard instantiateViewControllerWithIdentifier:@"PageViewController"];
    self.pageViewController.dataSource = self;
    self.pageViewController.delegate = self;
    
    // Change the size of page view controller
    self.pageViewController.view.frame = CGRectMake(0, 0, self.view.frame.size.width, self.view.frame.size.height);
    
    [self addChildViewController:_pageViewController];
    [self.view addSubview:_pageViewController.view];
    [self.pageViewController didMoveToParentViewController:self];
    
    // make sure the page controls are sitting on top
    [self.view bringSubviewToFront:self.pageControl];
    
    // get the movies
    PFQuery *query = [PFQuery queryWithClassName:@"Movie"];
    [query orderByDescending:@"updatedAt"];
    [query orderByAscending:@"index"];
    query.limit = 16;
    
    [query findObjectsInBackgroundWithBlock:^(NSArray *movies, NSError *error) {
        if (!error) {
            self.movies = movies;
            [self.pageControl setNumberOfPages:self.movies.count];
            
            MovieContentViewController *movieContentViewController = [self viewControllerAtIndex:0];
            NSArray *viewControllers = @[movieContentViewController];
            
            [self.pageViewController setViewControllers:viewControllers direction:UIPageViewControllerNavigationDirectionForward animated:NO completion:nil];
        } else {
            NSLog(@"Error!");
        }
    }];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (MovieContentViewController *)viewControllerAtIndex:(NSUInteger)index
{
    if (([self.movies count] == 0) || (index >= [self.movies count])) {
        return nil;
    }
    
    MovieContentViewController *movieContentViewController = [self.storyboard instantiateViewControllerWithIdentifier:@"MovieContentViewController"];
    
    PFObject *movie = self.movies[index];
    PFObject *posters = movie[@"posters"];
    PFObject *ratings = movie[@"ratings"];
    int rating = [[ratings objectForKey:@"critics_score"] intValue];
    
    movieContentViewController.titleText = movie[@"title"];
    movieContentViewController.ratingText = [NSString stringWithFormat:@"%d", rating];
    movieContentViewController.pageIndex = index;
    movieContentViewController.posterUrl = posters[@"detailed"];
    movieContentViewController.synopsisText = movie[@"synopsis"];
    movieContentViewController.mpaaRating = movie[@"mpaa_rating"];
    movieContentViewController.cast = movie[@"abridged_cast"];
    movieContentViewController.youtubeId = movie[@"youtubeId"];
    
    // calculate the duration
    int runTime = [movie[@"runtime"] intValue];
    NSString *hours = [NSString stringWithFormat:@"%d", runTime / 60];
    NSString *minutes = [NSString stringWithFormat:@"%d", runTime % 60];
    NSString *duration = [NSString stringWithFormat:@"%@h %@m", hours, minutes];
    
    movieContentViewController.duration = duration;
    
    return movieContentViewController;
}

- (UIViewController *)pageViewController:(UIPageViewController *)pageViewController viewControllerBeforeViewController:(UIViewController *)viewController
{
    NSUInteger index = ((MovieContentViewController *) viewController).pageIndex;
    
    if ((index == 0) || (index == NSNotFound)) {
        return nil;
    }
    
    index--;
    
    return [self viewControllerAtIndex:index];
}

- (UIViewController *)pageViewController:(UIPageViewController *)pageViewController viewControllerAfterViewController:(UIViewController *)viewController
{
    NSUInteger index = ((MovieContentViewController *) viewController).pageIndex;
    
    if (index == NSNotFound) {
        return nil;
    }
    
    index++;
    
    if (index == [self.movies count]) {
        return nil;
    }
    
    return [self viewControllerAtIndex:index];
}

- (void)pageViewController:(UIPageViewController *)pageViewController didFinishAnimating:(BOOL)finished previousViewControllers:(NSArray *)previousViewControllers transitionCompleted:(BOOL)completed
{
    if (!completed)
    {
        return;
    }
    
    // get the index of the current page and show it in the page controls
    MovieContentViewController *currentController = self.pageViewController.viewControllers[0];
    NSUInteger currentIndex = currentController.pageIndex;
    self.pageControl.currentPage = currentIndex;
}

@end
