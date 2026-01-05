import { Link } from 'react-router-dom';
import { JOB_CATEGORIES, JobCategory } from '@/types/job';

interface CategoryCardProps {
  category: typeof JOB_CATEGORIES[number];
  jobCount?: number;
}

export function CategoryCard({ category, jobCount = 0 }: CategoryCardProps) {
  return (
    <Link
      to={`/jobs?category=${category.value}`}
      className="group block bg-card rounded-xl border border-border p-6 card-hover"
    >
      <div className="flex items-start gap-4">
        <div className="flex h-14 w-14 items-center justify-center rounded-xl bg-accent/10 text-3xl group-hover:bg-accent/20 transition-colors">
          {category.icon}
        </div>
        <div className="flex-1">
          <h3 className="font-semibold text-foreground group-hover:text-accent transition-colors">
            {category.label}
          </h3>
          <p className="text-sm text-muted-foreground mt-1 line-clamp-2">
            {category.description}
          </p>
          <p className="text-xs text-accent font-medium mt-3">
            {jobCount} jobs available
          </p>
        </div>
      </div>
    </Link>
  );
}
