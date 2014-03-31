//
//  MovieContentViewController.h
//  NowPlaying
//
//  Created by Kyle Buchanan on 12/30/13.
//  Copyright (c) 2013 Kyle Buchanan. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface MovieContentViewController : UIViewController

@property (weak, nonatomic) IBOutlet UILabel *titleLabel;
@property (weak, nonatomic) IBOutlet UILabel *ratingLabel;
@property (weak, nonatomic) IBOutlet UIImageView *posterImageView;
@property NSUInteger pageIndex;
@property NSString *titleText;
@property NSString *ratingText;
@property NSString *posterUrl;
@property NSString *synopsisText;
@property NSString *mpaaRating;
@property NSString *duration;
@property NSString *youtubeId;
@property NSArray *cast;

@end
