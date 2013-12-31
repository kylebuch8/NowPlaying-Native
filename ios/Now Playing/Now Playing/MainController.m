//
//  MainController.m
//  Now Playing
//
//  Created by Kyle Buchanan on 10/12/13.
//  Copyright (c) 2013 Kyle Buchanan. All rights reserved.
//

#import "MainController.h"
#import "MovieView.h"

@interface MainController ()

@end

@implementation MainController

@synthesize movieScrollView;

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
    // Do any additional setup after loading the view from its nib.
    
    MovieView *movieView = [[MovieView alloc] init];
    [self.movieScrollView addSubview:movieView];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

@end
