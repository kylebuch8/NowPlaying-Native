//
//  MovieDetailViewController.m
//  NowPlaying
//
//  Created by Kyle Buchanan on 1/9/14.
//  Copyright (c) 2014 Kyle Buchanan. All rights reserved.
//

#import "MovieDetailViewController.h"
#import "MovieTrailerViewController.h"
#import "UIImage+ImageEffects.h"
#import <SDWebImage/UIImageView+WebCache.h>
#import <Parse/Parse.h>

@interface MovieDetailViewController ()

@property CGFloat lastYPosition;

@end

@implementation MovieDetailViewController

- (void)viewDidLoad
{
    [super viewDidLoad];
    
    // let the screen go under the navbar
    self.automaticallyAdjustsScrollViewInsets = NO;
        
    self.titleLabel.text = self.titleText;
    self.ratingLabel.text = [self.ratingText stringByAppendingString:@"%"];
    self.ratingAndDurationLabel.text = [NSString stringWithFormat:@"%@, %@", self.mpaaRating, self.duration];
    self.synopsisLabel.text = self.synopsisText;
    
    NSURL *posterURL = [NSURL URLWithString:self.posterUrl];
    SDWebImageManager *manager = [SDWebImageManager sharedManager];
    
    [manager downloadWithURL:posterURL options:0 progress:^(NSUInteger receivedSize, long long expectedSize) {
        
    } completed:^(UIImage *image, NSError *error, SDImageCacheType cacheType, BOOL finished) {
        UIColor *tintColor = [UIColor colorWithWhite:0.11 alpha:0.73];
        UIImage *backgroundImage = [image applyBlurWithRadius:2 tintColor:tintColor saturationDeltaFactor:1.8 maskImage:nil];
        
        [self.posterBgImageView setImage:backgroundImage];
        [self.posterImageView setImage:image];
    }];
    
    UITapGestureRecognizer *singleTap = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(tapHandler)];
    singleTap.numberOfTapsRequired = 1;
    [self.posterImageView addGestureRecognizer:singleTap];
    
    self.navigationController.navigationBar.tintColor = [UIColor whiteColor];
}

- (void)viewDidAppear:(BOOL)animated
{
    [super viewDidAppear:animated];
    
    // log the screen in google analytics
    self.screenName = @"About Screen";
    
    // get the position of the separator line in the view
    CGFloat startingYPosition = self.castSeparatorView.frame.origin.y + self.castSeparatorView.frame.size.height + 10;
    CGFloat startingXPosition = 20;
    CGFloat labelWidth = self.view.frame.size.width - 40;
    CGFloat labelHeight = 20;
    UIFont *font = [UIFont systemFontOfSize:13];
    UIColor *lightGrey = [UIColor colorWithRed:(190/255.0) green:(190/255.0) blue:(190/255.0) alpha:1.0]; // #bebebe
    UIColor *separatorBGColor = [UIColor colorWithRed:(239/255.0) green:(239/255.0) blue:(239/255.0) alpha:1.0]; // #efefef
    int separatorHeight = 1;
    int i;
    
    for (i = 0; i < self.cast.count; i++) {
        PFObject *castMember = [self.cast objectAtIndex:i];
        NSString *castMemberNameString = castMember[@"name"];
        NSArray *castMemberCharacters = castMember[@"characters"];
        NSString *castMemberCharacterString = [castMemberCharacters objectAtIndex:0];
        
        UILabel *castMemberName = [[UILabel alloc] initWithFrame:CGRectMake(startingXPosition, startingYPosition, labelWidth, labelHeight)];
        castMemberName.autoresizingMask = UIViewAutoresizingFlexibleWidth;
        castMemberName.font = font;
        castMemberName.text = castMemberNameString;
        
        [self.scrollView addSubview:castMemberName];
        
        startingYPosition = castMemberName.frame.origin.y  + labelHeight;
        
        if (castMemberCharacterString) {
            UILabel *castMemberCharacterLabel = [[UILabel alloc] initWithFrame:CGRectMake(startingXPosition, startingYPosition, labelWidth, labelHeight)];
            castMemberCharacterLabel.font = font;
            [castMemberCharacterLabel setTextColor:lightGrey];
            castMemberCharacterLabel.text = castMemberCharacterString;
            
            [self.scrollView addSubview:castMemberCharacterLabel];
        
            startingYPosition = castMemberCharacterLabel.frame.origin.y + labelHeight + (labelHeight / 2);
        } else {
            // we need to bump it down a bit since we don't have the character name above it
            startingYPosition += (labelHeight / 2);
        }
        
        UIView *separator = [[UIView alloc] initWithFrame:CGRectMake(startingXPosition, startingYPosition, labelWidth, separatorHeight)];
        separator.autoresizingMask = UIViewAutoresizingFlexibleWidth;
        [separator setBackgroundColor:separatorBGColor];
        
        [self.scrollView addSubview:separator];

        startingYPosition = separator.frame.origin.y + (labelHeight / 2);
    }
    
    // set the last y position so we know how big to make the scroll view
    self.lastYPosition = startingYPosition + labelHeight;
}

- (void)viewDidLayoutSubviews
{
    [self.scrollView setContentSize:CGSizeMake(self.scrollView.frame.size.width, self.lastYPosition)];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)tapHandler
{
    [self performSegueWithIdentifier:@"DetailToTrailer" sender:self];
}

- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender
{
    if ([[segue identifier] isEqualToString:@"DetailToTrailer"]) {
        MovieTrailerViewController *movieTrailerViewController = [segue destinationViewController];
        movieTrailerViewController.youtubeId = self.youtubeId;
    }
}

@end
