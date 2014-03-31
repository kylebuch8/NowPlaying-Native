//
//  MovieContentViewController.m
//  NowPlaying
//
//  Created by Kyle Buchanan on 12/30/13.
//  Copyright (c) 2013 Kyle Buchanan. All rights reserved.
//

#import "MovieContentViewController.h"
#import "UIImage+ImageEffects.h"
#import <SDWebImage/UIImageView+WebCache.h>
#import "MovieDetailViewController.h"

@interface MovieContentViewController ()

@property (weak, nonatomic) IBOutlet UIImageView *posterBgImageView;

@end

@implementation MovieContentViewController

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
    
	self.titleLabel.text = self.titleText;
    self.ratingLabel.text = [self.ratingText stringByAppendingString:@"%"];
    
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
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)tapHandler
{
    [self performSegueWithIdentifier:@"MovieToDetail" sender:self];
}

- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender
{
    if ([[segue identifier] isEqualToString:@"MovieToDetail"]) {
        MovieDetailViewController *movieDetailViewController = [segue destinationViewController];
        movieDetailViewController.titleText = self.titleText;
        movieDetailViewController.posterUrl = self.posterUrl;
        movieDetailViewController.synopsisText = self.synopsisText;
        movieDetailViewController.ratingText = self.ratingText;
        movieDetailViewController.mpaaRating = self.mpaaRating;
        movieDetailViewController.duration = self.duration;
        movieDetailViewController.cast = self.cast;
        movieDetailViewController.youtubeId = self.youtubeId;
    }
}

@end
