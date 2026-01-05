import { Link } from 'react-router-dom';
import { Job } from '@/types/job';
import { Badge } from '@/components/ui/badge';
import { Button } from '@/components/ui/button';
import { MapPin, Clock, DollarSign, ArrowRight } from 'lucide-react';
import { formatDistanceToNow } from 'date-fns';

interface JobCardProps {
  job: Job;
}

export function JobCard({ job }: JobCardProps) {
  const formatSalary = () => {
    const { min, max, currency, period } = job.salary;
    return `${currency} ${min}-${max}/${period}`;
  };

  return (
    <article className="group bg-card rounded-xl border border-border p-6 card-hover">
      <div className="flex flex-col gap-4">
        {/* Header */}
        <div className="flex items-start justify-between gap-4">
          <div className="flex-1">
            <div className="flex flex-wrap items-center gap-2 mb-2">
              {job.featured && <Badge variant="featured">Featured</Badge>}
              {job.urgent && <Badge variant="urgent">Urgent</Badge>}
              <Badge variant="category">{job.category.replace('-', ' ')}</Badge>
            </div>
            <h3 className="text-lg font-semibold text-foreground group-hover:text-accent transition-colors">
              <Link to={`/jobs/${job.slug}`}>{job.title}</Link>
            </h3>
            <p className="text-muted-foreground mt-1">{job.company}</p>
          </div>
          <Badge variant="secondary" className="shrink-0">{job.type}</Badge>
        </div>

        {/* Meta Info */}
        <div className="flex flex-wrap gap-4 text-sm text-muted-foreground">
          <span className="flex items-center gap-1.5">
            <MapPin className="h-4 w-4 text-accent" />
            {job.location}
          </span>
          <span className="flex items-center gap-1.5">
            <DollarSign className="h-4 w-4 text-accent" />
            {formatSalary()}
          </span>
          <span className="flex items-center gap-1.5">
            <Clock className="h-4 w-4 text-accent" />
            {formatDistanceToNow(new Date(job.postedAt), { addSuffix: true })}
          </span>
        </div>

        {/* Description */}
        <p className="text-muted-foreground text-sm line-clamp-2">
          {job.description}
        </p>

        {/* Skills */}
        <div className="flex flex-wrap gap-2">
          {job.skills.slice(0, 4).map((skill) => (
            <Badge key={skill} variant="muted" className="text-xs">
              {skill}
            </Badge>
          ))}
          {job.skills.length > 4 && (
            <Badge variant="muted" className="text-xs">+{job.skills.length - 4}</Badge>
          )}
        </div>

        {/* Action */}
        <div className="flex items-center justify-between pt-2 border-t border-border">
          <span className="text-xs text-muted-foreground">
            Apply before {new Date(job.deadline).toLocaleDateString()}
          </span>
          <Button variant="ghost" size="sm" asChild className="group/btn">
            <Link to={`/jobs/${job.slug}`}>
              View Details
              <ArrowRight className="h-4 w-4 transition-transform group-hover/btn:translate-x-1" />
            </Link>
          </Button>
        </div>
      </div>
    </article>
  );
}
