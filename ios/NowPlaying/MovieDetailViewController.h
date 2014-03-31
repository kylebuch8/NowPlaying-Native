//
//  MovieDetailViewController.h
//  NowPlaying
//
//  Created by Kyle Buchanan on 1/9/14.
//  Copyright (c) 2014 Kyle Buchanan. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "GAITrackedViewController.h"

@interface MovieDetailViewController : GAITrackedViewController

@property (weak, nonatomic) IBOutlet UIScrollView *scrollView;
@property (weak, nonatomic) IBOutlet UILabel *titleLabel;
@property (weak, nonatomic) IBOutlet UIImageView *posterImageView;
@property (weak, nonatomic) IBOutlet UIImageView *posterBgImageView;
@property (weak, nonatomic) IBOutlet UILabel *synopsisLabel;
@property (weak, nonatomic) IBOutlet UILabel *ratingLabel;
@property (weak, nonatomic) IBOutlet UILabel *ratingAndDurationLabel;
@property (weak, nonatomic) IBOutlet UIView *castSeparatorView;
@property NSString *titleText;
@property NSString *ratingText;
@property NSString *posterUrl;
@property NSString *synopsisText;
@property NSString *mpaaRating;
@property NSString *duration;
@property NSString *youtubeId;
@property NSArray *cast;

@end
